#from flask import Flask
from flask import Flask ,jsonify , \
render_template , request , url_for,\
 redirect , Response , json, send_from_directory
from flask.ext.pymongo import PyMongo
import datetime
from bson.json_util import dumps
from bson.objectid import ObjectId
import os
app = Flask(__name__)
app.config.from_object(__name__)

app.config['MONGO_DBNAME'] = 'database_Notes'
#import views
APP_ROOT = os.path.dirname(os.path.abspath(__file__))
UPLOAD_FOLDER = os.path.join(APP_ROOT, 'data/notes/')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

ALLOWED_EXTENSIONS = set(['pdf'])


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



@app.route('/insertSubject',methods = ['POST'])
def insertSubject():
	name = request.form.get('name')
	courseId = request.form.get('id')
	sem = request.form.get('sem')
	new_sub = {"name" :name,"semester" : sem,"course_id" :courseId}
	Sub_id = mongo.db.Subjects.insert(new_sub)
	
	return jsonify(success = True, id = str(Sub_id))

@app.route('/getAllSubjects',methods = ['GET'])
def getSubjects():
	subList = []
	i=0
	for instance in mongo.db.Subjects.find():
		subList.insert(i,{'SubjectId' : instance['course_id'], 'name' : instance['name'],'semester' : instance['semester']})
		i = i+1
	return   Response(json.dumps(subList),  mimetype='application/json')
	

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.',1)[1] in ALLOWED_EXTENSIONS



@app.route('/addnote',methods = ['POST'] )
def addnote():
	sem = request.form.get('sem')
	file = request.files['file']
	course_id = request.form.get('courseId')
	term = request.form.get('term')
	teacher = request.form.get('teacher')
	#str(course_id)+str(teacher)
	
	note = { "semester" : sem,"term" : term,"course_id" : course_id, "teacherName" : teacher}
	
	
	if allowed_file(file.filename):
		
		path = (app.config['UPLOAD_FOLDER'])
		if (not os.path.isdir(path)):
			os.mkdir(path)

		path = path +sem 

		if (not os.path.isdir(path)):
			os.mkdir(path)

		path = path + '/'+course_id

		if(not os.path.isdir(path)):
			os.mkdir(path)
		noteId = mongo.db.notes.insert(note)
		file.save(path +'/'+ str(noteId))
	return jsonify(success = True, id = str(noteId)) 

@app.route('/getAllNotes',methods = ['GET','POST'])
def getAllNotes():
	i = 0
	noteList = []
	for instance in mongo.db.notes.find():
		noteList.insert(i,{'pdfId' : str(instance['_id']),'term' : instance['term'] , 'semester' : instance['semester'] , \
			'teacherName' : instance['teacherName'] , 'course_id' : instance['course_id'] , 'rating' : instance['rating']})
		i = i + 1
	return   Response(json.dumps(noteList),  mimetype='application/json')

@app.route('/downloadNote',methods = ['GET'])
def downlaodPdf():
	sem = request.args.get('semester')
	pdfId = request.args.get('id')
	path =  app.config['UPLOAD_FOLDER']
	print str(sem) + ' ' + str(pdfId)
	note = mongo.db.notes.find_one({"_id" : ObjectId(pdfId)})
	if note != None:
		path = path + sem + '/'+ note['course_id']
		print path
		return send_from_directory(path , pdfId)
	return path

@app.route('/rateNote', methods = ['POST'])
def postRate():
	pdfId = request.form.get('pdfId')
	rating = float(request.form.get('rating'))
	note = mongo.db.notes.find_one( { "_id"  : ObjectId (pdfId) })
	rating = rating + (note['rating'] * note['numberRating'])
	numberRating = note['numberRating'] + 1
	rating = float(rating) / numberRating
	returnId = mongo.db.notes.update_one({ "_id"  : ObjectId (pdfId) } , {"$set":{"rating" : rating , "numberRating" : numberRating}})
	return jsonify(success  = True)