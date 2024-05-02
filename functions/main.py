# Welcome to Cloud Functions for Firebase for Python!
# To get started, simply uncomment the below code or create your own.
# Deploy with `firebase deploy`

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium import webdriver


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



def find_original_acronym(fake_acronym):
    # we have an acronym that points us to a webstite that contains the original acronym
    # f"https://search.epfl.ch/?filter=unit&q={acronym}"
    
    # Specify the URL of the webpage you want to scrape
    url = f"https://search.epfl.ch/?filter=unit&q={fake_acronym}"  # Replace with your target URL

    # Configure Chrome options
    chrome_options = Options()
    chrome_options.binary_location = "./google-chrome-stable_current_amd64.deb"
    # Replace with your Chrome binary location
    chrome_options.add_argument("--headless")  # Run Chrome in headless mode, i.e., without opening a GUI
    chrome_options.add_argument("--disable-gpu")  # Disable GPU acceleration (necessary in headless mode)
    try:
        driver = webdriver.Chrome( options=chrome_options)
    except Exception as e:
        print(f"An error occurred: {e}")
        return list()

    try:
        # Open the webpage
        driver.get(url)

        # Wait for the content to load (adjust timeout and element locator as needed)
        WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.CSS_SELECTOR, "ul li")))

        # Extract desired content using Selenium
        elements = driver.find_elements(By.CSS_SELECTOR, "ul li")
        for element in elements:
            # we want to get the element that contains the acronym
            if fake_acronym in element.text:
                # we want to get the first element that contains the acronym
                # return the string expect the last string
                return element.text.split(" ")[:-1]
            
    except Exception as e:
        print(f"An error occurred: {e}")
        return list()
    finally:
        # Close the browser
        driver.quit()


@https_fn.on_call(region="europe-west6")
def oncallFind(req: https_fn.Request) -> https_fn.Response:
    # the request body is a JSON object with a single key "email"
    email = req.data["email"]
    firestore_client: google.cloud.firestore.Client = firestore.Client()

    userID = req.auth.uid
    try:
        profile = epflpeople.find(email)
    except:
        profile = None
        
    

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
            
            acronymList = find_original_acronym(profile[0]["accreds"][i + 1]["acronym"])
            print(acronymList)
            acronym = " ".join(acronymList)
            print(acronym)
            
            #find id of the association in the database 
            association = firestore_client.collection("associations").where("acronym_lower", "==",acronym.lower()).stream()
            association = list(association)
            if len(association) == 0:
                print("could not find association")
                continue
            id = association[0].id
            print(f"id = {id}")

            temp = {"id": id,
            "position": profile[0]["accreds"][i + 1]["position"],
            "rank": profile[0]["accreds"][i + 1]["rank"]}
                
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
        

        print(user)
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
    
    



