
import boto3
from boto3 import client

from userprovider.cognito import _parse_user, get_user_info

def _get_user_entry(name, value):
    return {
        "Name": name,
        "Value": value
    }


def _get_user_data():
    return {
        "Attributes": [
            _get_user_entry("name", "some name"),
            _get_user_entry("sub", "some-sub"),
            _get_user_entry("email", "some@email.no")
        ]
    }


def test_parse_user():
    user_data = _parse_user(_get_user_data())
    _validate_user_data(user_data)


def test_get_user_info(mocker):
    mocker.patch.object(boto3, "client")
    client = boto3.client("cognito-idp")

    boto3.client.return_value = client
    mocker.patch.object(client, "list_users")

    client.list_users.return_value = {
        "Users": [_get_user_data()]
    }

    user_data = get_user_info("some-sub")
    _validate_user_data(user_data)


def _validate_user_data(user_data):
    assert "name" in user_data and user_data["name"] == "some name"
    assert "sub" in user_data and user_data["sub"] == "some-sub"
    assert "email" in user_data and user_data["email"] == "some@email.no"
