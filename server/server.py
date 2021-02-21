import sqlite3
from sqlite3 import Error
import pandas as pd
from pydantic import BaseModel
from fastapi import FastAPI, status

app = FastAPI()


class UserCredentials(BaseModel):
    username: str
    password: str

class Message(BaseModel):
    sent_by: str
    date_time: str
    message: str


def create_connection(path):
    connection = None
    try:
        connection = sqlite3.connect(path)
        print("Connection to SQLite DB successful")
    except Error as e:
        print(f"The error '{e}' occurred")

    return connection


def execute_query(connection, query):
    cursor = connection.cursor()
    try:
        cursor.execute(query)
        connection.commit()
        print("Query executed successfully")
    except Error as e:
        print(f"The error '{e}' occurred")


def add_user_query(uname, pswd):
    print("User added successfully")
    return f'''INSERT INTO users (username, password) VALUES ("{uname}", "{pswd}");'''


def send_message_query(sent_by, date_time, message):
    print(f"{sent_by} at {date_time} : {message}")
    return f'''INSERT INTO messages (sent_by, date_time, message) VALUES ("{sent_by}", "{message}", "{date_time}");'''

create_user_table_query = '''
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    password TEXT NOT NULL
);
'''

create_message_table_query = '''
CREATE TABLE IF NOT EXISTS messages (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sent_by TEXT NOT NULL,
    date_time TEXT NOT NULL,
    message TEXT NOT NULL
)
'''

user_connection = create_connection("user_db.sqlite")
execute_query(user_connection, create_user_table_query)

messages_connection = create_connection("messages_db.sqlite")
execute_query(messages_connection, create_message_table_query)


@app.post("/signup", status_code=status.HTTP_201_CREATED)
async def add_user(credentials: UserCredentials):
    query = add_user_query(credentials.username, credentials.password)
    execute_query(user_connection, query)
    return 1


@app.post("/checkuser", status_code=status.HTTP_200_OK)
async def check_user(credentials: UserCredentials):
    query = f"SELECT username FROM users WHERE username='{credentials.username}';"
    query_response = pd.read_sql_query(query, user_connection)
    user_exists: bool = not query_response.empty
    return user_exists


@app.post("/login", status_code=status.HTTP_200_OK)
async def login(credentials: UserCredentials):
    try:
        query = f"SELECT username, password FROM users WHERE username='{credentials.username}';"
        query_response = pd.read_sql_query(query, user_connection)
        user_password = query_response.get("password")[0]
        if user_password == credentials.password:
            return True
        return False

    except:
        return False

@app.post("/sendmessage", status_code=status.HTTP_200_OK)
async def send_message(message: Message):
    try:
        query = send_message_query(message.sent_by, message.date_time, message.message)
        execute_query(messages_connection, query)
        return True
    except:
        return False