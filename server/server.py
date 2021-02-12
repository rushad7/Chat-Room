import sqlite3
from sqlite3 import Error
import pandas as pd
from pydantic import BaseModel
from fastapi import FastAPI, status

app = FastAPI()


class Data(BaseModel):
    username: str
    password: str


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


create_user_table_query = '''
CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT NOT NULL,
  password TEXT NOT NULL
);
'''

connection = create_connection("user_db.sqlite")
execute_query(connection, create_user_table_query)


@app.post("/adduser", status_code=status.HTTP_201_CREATED)
async def add_user(credentials: Data):
    query = add_user_query(credentials.username, credentials.password)
    execute_query(connection, query)
    return {"response": "user added successfully"}


@app.post("/checkuser", status_code=status.HTTP_100_CONTINUE)
async def check_user(credentials: Data):
    query = f"SELECT username FROM users WHERE username='{credentials.username}';"
    query_response = pd.read_sql_query(query)
    if query_response.empty:
        return 0
    
    return 1