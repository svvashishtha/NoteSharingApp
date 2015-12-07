from flask import Flask ,jsonify , \
render_template , request , url_for, redirect , Response , json
from flask.ext.pymongo import PyMongo
import datetime
from bson.json_util import dumps
from app import app
mongo = PyMongo(app)

@app.route('/insertSubject',methods = ['POST'])
def insertSubject():
	name = request.form.get('name')
	courseId = request.form.get('id')
	sem = request.form.get('sem')
	new_sub = {"name" :name,"semester" : sem,"course_id" :courseId}
	Sub_id = mongo.db.insert(new_sub)
	
	return jsonify(success = True, id = str(Sub_id))
