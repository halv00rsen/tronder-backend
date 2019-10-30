
from json import loads
from userprovider.userinfo import is_valid_json

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


def test_valid_request(client):
    response = client.post(URL, json={
        "sub": "some-sub"
    })
    assert response.status_code == 200
    data = loads(response.data)
    assert isinstance(data, dict)
    assert "info" in data
    assert data["info"] == "some-sub"
