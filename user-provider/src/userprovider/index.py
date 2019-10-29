
from flask import Flask, Blueprint, request, jsonify, abort
userinfo = Blueprint('userinfo', __name__, url_prefix="/userinfo")

def is_valid_json(json):
    if "sub" not in json or not isinstance(json["sub"], str):
        return False
    else:
        return True


@userinfo.route("/", methods=["GET"])
def get_user_info():
    data = request.get_json(silent=True)
    if data is None or not is_valid_json(data):
        abort(400)
    user_key = data["sub"]
    return jsonify({
        "info": user_key
    })


@userinfo.errorhandler(404)
def path_not_found(error):
    return jsonify({
        "error": "page not found"
    }), 404

@userinfo.errorhandler(400)
def error_input_json(error):
    return jsonify({
        "error": "user data provided are on wrong format"
    }), 400
