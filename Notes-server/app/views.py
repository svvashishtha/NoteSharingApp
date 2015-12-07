from flask import Flask ,jsonify , \
render_template , request , url_for, redirect , Response , json
from flask.ext.pymongo import PyMongo
import datetime
from app import app
from bson.json_util import dumps
app = Flask(__name__)

app.config.from_object(__name__)

app.config['MONGO_DBNAME'] = 'database_Notes'
mongo = PyMongo(app, config_prefix='MONGO')



@app.route('/register/',methods = ['POST'])
def insertUser():
	if request.method == 'POST':
		username = request.form['username']
		password = request.form['password']
		fullname = request.form['fullname']
		email = request.form['email']
		new_user = {"username": username,"password": password,"fullname": fullname,"email" : email,"date" : datetime.datetime.utcnow()}
		userid = mongo.db.users.insert(new_user)
		return jsonify(userId = str(userid))

@app.route('/login',methods = ['GET'])
def login():
	email = request.args.get('email')
	password = request.args.get('password')
	user = mongo.db.users.find_one({"email":email , "password":password})
	if user != None:
		return jsonify(fullname = user['fullname'] , email = user['email'] , userid = str(user['_id']))
	return jsonify(success = false)

@app.route('/userList',methods = ['GET'])
def lsUsers():
	userList = []
	i = 0
	for instance in mongo.db.users.find():
		userList.insert(i,{'userId' : str(instance['_id']), 'email' : instance['email'], 'fullName' : instance['fullname']})
		i = i+1
	return Response(json.dumps(userList),  mimetype='application/json')
