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


@app.post("/signup", status_code=status.HTTP_201_CREATED)
async def add_user(credentials: Data):
    query = add_user_query(credentials.username, credentials.password)
    execute_query(connection, query)
    return 1


@app.post("/checkuser", status_code=status.HTTP_200_OK)
async def check_user(credentials: Data):
    query = f"SELECT username FROM users WHERE username='{credentials.username}';"
    query_response = pd.read_sql_query(query, connection)
    user_exists: bool = not query_response.empty
    return user_exists

@app.post("/login", status_code=status.HTTP_200_OK)
async def login(credentials: Data):
	try:
		query = f"SELECT username, password FROM users WHERE username='{credentials.username}';"
		query_response = pd.read_sql_query(query, connection)
		user_password = query_response.get("password")[0]
		if user_password == credentials.password:
			return True
		return False
		
	except: 
		return False
