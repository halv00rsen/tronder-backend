
from json import loads

from userprovider.userinfo import is_valid_json
import userprovider.cognito as cognito

URL = "/userinfo/"

def test_valid_json():
    assert not is_valid_json({"someKey": "some value"})
    assert not is_valid_json({"sub": 1})
    assert is_valid_json({"sub": "some-sub-value"})


def test_error_status_codes(client):
    assert client.get(URL).status_code == 405
    assert client.post("/userinfo").status_code == 308
    assert client.post(URL).status_code == 400


def test_invalid_json(client):
    assert client.post(URL, json={
        "someKey": "someValue"
    }).status_code == 400
    assert client.post(URL, json={
        "sub": 1
    }).status_code == 400


def test_valid_request(client, mocker):
    mock_data = {"sub": "some-sub"}

    mocker.patch.object(cognito, "get_user_info")
    cognito.get_user_info.return_value = mock_data

    response = client.post(URL, json=mock_data)
    cognito.get_user_info.assert_called_with(mock_data["sub"])

    assert response.status_code == 200
    data = loads(response.data)
    assert isinstance(data, dict)
    assert "sub" in data
    assert data["sub"] == "some-sub"


def test_valid_response(client, mocker):
    mock_data = {
        "sub": "some-sub",
        "name": "some name",
        "email": "some-mail"
    }
    mocker.patch.object(cognito, "get_user_info")
    cognito.get_user_info.return_value = mock_data

    response = client.post(URL, json=mock_data)

    data = loads(response.data)
    validate_values(data, "name", "email", "sub")


def test_invalid_sub(client, mocker):
    mocker.patch.object(cognito, "get_user_info")
    cognito.get_user_info.return_value = None
    response = client.post(URL, json={"sub": "some-sub"})
    assert response.status_code == 404


def validate_values(data, *keys):
    for key in keys:
        assert key in data and isinstance(data[key], str)
