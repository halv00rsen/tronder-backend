
import pytest

from flask.testing import FlaskClient

from userprovider import create_app

@pytest.fixture
def app():
    internal_app = create_app()
    internal_app.test_client_class = FlaskClient
    return internal_app

@pytest.fixture
def client(app):
    internal_client = app.test_client()
    yield internal_client
