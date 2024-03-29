
from firebase_functions import https_fn, options

# Dependencies for writing to Realtime Database.
from firebase_admin import db, initialize_app

import epflpeople


@https_fn.on_call()
def oncallFind(req: https_fn.Request) -> https_fn.Response:
    
    return None
