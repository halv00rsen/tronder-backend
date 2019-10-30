
import boto3

def get_user_info(sub):
    client = boto3.client('cognito-idp')
    response = client.list_users(
        UserPoolId='eu-west-1_2Kpe7kp5u',
        Limit=1,
        AttributesToGet=[
            "name",
            "email",
            "sub"
        ],
        Filter="sub = \"{:s}\"".format(sub)
    )
    users = response["Users"]
    if len(users) == 1:
        return _parse_user(users[0])
    else:
        return None


def _parse_user(user):
    user_data = dict()
    for attribute in user["Attributes"]:
        user_data[attribute["Name"]] = attribute["Value"]
    return user_data


if __name__ == "__main__":
    print(get_user_info("c3ce1b09-5baa-4687-9449-65b93a5f2852"))
