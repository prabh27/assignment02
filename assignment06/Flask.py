from flask import jsonify
from flask_sqlalchemy import SQLAlchemy
from flask import Flask, request, flash, url_for, redirect, render_template
import time
from datetime import datetime


app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://prabh_simran:prabh_simran@aline-cnu-insights-dev-cluster.cluster-czuocyoc6awe.us-east-1.rds.amazonaws.com/cnu2016_prabh_simran'
db = SQLAlchemy(app)

class AuditLog(db.Model):
    id = db.Column(db.INT, primary_key = True)
    timestamp = db.Column(db.DATETIME)
    request_type = db.Column(db.VARCHAR)
    parameters = db.Column(db.VARCHAR)
    url = db.Column(db.VARCHAR)
    response_code = db.Column(db.INT)
    request_ip_address = db.Column(db.VARCHAR)
    request_duration_ms = db.Column(db.VARCHAR)

    def __init__(self, timestamp, request_type, parameters, url, response_code, request_ip_address, request_duration_ms, id):
        self.timestamp = self.timestamp,
        self.id = id
        self.request_type = request_type
        self.parameters = parameters
        self.url = url
        self.response_code = response_code
        self.request_ip_address = request_ip_address
        self.request_duration_ms = request_duration_ms

    def as_json(self):
        ts = datetime.strptime((str(self.timestamp)), '%Y-%m-%d %H:%M:%S').strftime('%m/%d/%YT%H:%M:%S')
        print self.timestamp
        return {
            "timestamp": ts,
            "url": self.url,
            "request_type": self.request_type,
            "parameters": self.parameters,
            "request_duration_ms": int(self.request_duration_ms),
            "response_code": int(self.response_code),
            "request_ip_address": self.request_ip_address
        }


@app.route('/api/auditLogs')
def show_all():
        selectQuery = AuditLog.query
        startTime = request.args.get('startTime','12/10/1970T00:00:00')
        endTime = request.args.get('endTime','1/1/2030T00:00:00')
        startTime = startTime.split('T')
        nowTime = startTime[0] + " " + startTime[1]
        date_object1 = datetime.strptime(nowTime, '%m/%d/%Y %H:%M:%S')
        endTime = endTime.split('T')
        nowTime = endTime[0] + " " + endTime[1]
        date_object2 = datetime.strptime(nowTime, '%m/%d/%Y %H:%M:%S')
        print date_object1
        print date_object2
        selectQuery = selectQuery.filter(AuditLog.timestamp.between(date_object1, date_object2))
        offset = request.args.get('offset')
        if offset is None:
            offset = 0
        if(int(offset) > 10):
            offset = 10
        limit = request.args.get('limit')
        print limit
        print offset
        if limit is None:
            limit = 10
        if(int(limit) > 10):
            limit = 10
        return jsonify(
            data=[a.as_json() for a in selectQuery.order_by(AuditLog.timestamp.desc()).limit(limit).offset(offset).all()])
    # except:
    #      return jsonify(data = "error")

if __name__ == "__main__":
    db.create_all()
    app.run("localhost", 5000, debug='true')
