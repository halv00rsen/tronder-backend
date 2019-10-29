
from json import loads
from userprovider.index import is_valid_json

def test_valid_json():
    assert not is_valid_json({"someKey": "some value"})
    assert not is_valid_json({"sub": 1})
    assert is_valid_json({"sub": "some-sub-value"})


def test_get_user_info(client):
    url = "/userinfo/"
    assert client.get("/userinfo").status_code == 308
    assert client.get(url).status_code == 400
    assert client.get(url, json={
        "someKey": "someValue"
    }).status_code == 400
    assert client.get(url, json={
        "sub": 1
    }).status_code == 400
    response = client.get(url, json={
        "sub": "some-sub"
    })
    assert response.status_code == 200
    data = loads(response.data)
    assert isinstance(data, dict)
    assert "info" in data
    assert data["info"] == "some-sub"
