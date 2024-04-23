# Welcome to Cloud Functions for Firebase for Python!
# To get started, simply uncomment the below code or create your own.
# Deploy with `firebase deploy`

from firebase_functions import https_fn
from firebase_admin import initialize_app, db


from firebase_functions import https_fn, options
import epflpeople
from firebase_functions.firestore_fn import (
    on_document_written,
    Event,
    Change,
    DocumentSnapshot,
)
from flask import jsonify
from google.cloud import firestore
initialize_app()

@https_fn.on_call(region="europe-west6")
def oncallFind(req: https_fn.Request) -> https_fn.Response:
    # the request body is a JSON object with a single key "email"
    email = req.data["email"]


    userID = req.auth.uid
    profile = epflpeople.find(email)
    firestore_client: google.cloud.firestore.Client = firestore.Client()

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
            acronym = profile[0]["accreds"][i + 1]["acronym"]
            query = firestore_client.collection(f"associations").where("acronym", "==", acronym).limit(1).stream()
            for doc in query:
                association = doc.to_dict()
                temp = {
                    "id": doc.id,
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
            "associations" : associations,
            "following": []
        }
        

        
        firestore_client.document(f"users/{userID}").set(user)
        # transform profile insto a json format
        

        return {"response":"User is Found"}

    else:
        user = {
            "email": "",
            "name": "",
            "firstname": "",
            "sciper": "",
            "semester": "",
            "associations" : [],
            "following": []
        }
        firestore_client.document(f"users/{userID}").set(user)
        
        
        return {"response":f"could not find: {email}"}
