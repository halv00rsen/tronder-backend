
from flask import Flask

def create_app():
    app = Flask(__name__)

    from userprovider.userinfo import userinfo
    app.register_blueprint(userinfo)

    return app
