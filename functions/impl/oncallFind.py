from firebase_functions import https_fn, options

# Dependencies for writing to Realtime Database.
from firebase_admin import db, initialize_app
import epflpeople
from firebase_functions.firestore_fn import (
    on_document_written,
    Event,
    Change,
    DocumentSnapshot,
)
import google.cloud.firestore

initialize_app()


@https_fn.on_call()
def oncallFind(req: https_fn.Request) -> https_fn.Response:
    # the request body is a JSON object with a single key "email"
    email = req.data["email"]
    profile = epflpeople.get_profile(email)

    # profile is a format of a list of dictionnary we want first elem and change the dict into a json
    if profile:
        # {'name': 'Philippe', 'firstname': 'Marin Marc Alain'
        # , 'email': 'marin.philippe@epfl.ch'
        # , 'sciper': '326343'
        # , 'rank': 0
        # , 'accreds':
        #       [{'acronym': 'IN-MA2',
        #       'path': 'EPFL/ETU/IN-S/IN-MA2',
        #       'name': "Section d'informatique - Master semestre 2",
        #       'position': 'Etudiant',
        #       'phoneList': [],
        #       'officeList': [],
        #       'rank': 0, 'code': 50038}],
        # 'profile': 'marin.philippe'}
        associations = list()

        for i in range(len(profile[0]["accreds"]) - 1):
            temp = {
                "acronym": profile[0]["accreds"][i + 1]["acronym"],
                "position": profile[0]["accreds"][i + 1]["position"],
                "rank": profile[0]["accreds"][i + 1]["rank"],
            }
            associations.append(temp)
        user = {
            "email": profile[0]["email"],
            "name": profile[0]["name"],
            "firstname": profile[0]["firstname"],
            "sciper": profile[0]["sciper"],
            "semester": profile[0]["accreds"][0]["acronym"],
            "associations" : associations
        }
        firestore_client: google.cloud.firestore.Client = firestore.client()
        emailHash = hash(email)
        firestore_client.document(f"users").docuement(emailHash).set()
        return https_fn.Response(profile[0])
    else:
        return https_fn.Response("No profile found for email: " + email, status=404)
