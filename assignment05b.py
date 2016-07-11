import boto.sqs
import csv
from sqlalchemy import *
import schedule
import ast
import os

import pymysql
import time

conn = boto.sqs.connect_to_region(
    "us-east-1",
     aws_access_key_id=os.environ['AWS_ACCESS_KEY_ID'],
     aws_secret_access_key=os.environ['AWS_SECRET_ACCESS_KEY'])
myqueue = conn.get_queue('cnu2016_prabh_simran_assignment05a')
engine = create_engine("mysql+pymysql://root:prabh@localhost/cnu2016_prabh_simran")
connection = engine.connect()

metadata = MetaData()

AuditLog = Table('AuditLog', metadata, autoload=True, autoload_with=engine)
log_id = 1

while 1:
    print "... waiting messages ..."
    messages = myqueue.get_messages(wait_time_seconds=20, num_messages=10)
    for message in messages:
        d = ast.literal_eval(message._body)
        try:
            ins = AuditLog.insert().values(id=log_id, start_time=d['date'], request_url=d['url'], response_code=d['responseCode'], ip_address=d['ipAddress'])
            log_id = log_id + 1
            connection.execute(ins)
            myqueue.delete_message(message)
        except:
            print "Error"
    time.sleep(1)

connection.close()