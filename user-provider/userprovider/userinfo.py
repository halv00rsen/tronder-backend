
from flask import Flask, Blueprint, request, jsonify, abort, Response

import userprovider.cognito as cognito

userinfo = Blueprint('userinfo', __name__, url_prefix="/userinfo")

def is_valid_json(json):
    if "sub" not in json or not isinstance(json["sub"], str):
        return False
    else:
        return True


@userinfo.route("/", methods=["POST"])
def get_user_info():
    data = request.get_json(silent=True)
    if data is None or not is_valid_json(data):
        abort(400)
    user_info = cognito.get_user_info(data["sub"])
    if user_info is None:
        abort(404, "sub not found")
    else:
        return jsonify(user_info)


@userinfo.errorhandler(404)
def path_not_found(error):
    return jsonify({
        "error": str(error)
    }), 404


@userinfo.errorhandler(400)
def error_input_json(error):
    return jsonify({
        "error": str(error)
    }), 400
