
import csv
from sqlalchemy import *
from datetime import date
from time import strftime
import random
from random import randrange


engine = create_engine("mysql+pymysql://root:prabh@localhost/cnu2016_prabh_simran")
connection = engine.connect()

metadata = MetaData()
orders = Table('Orders', metadata, autoload=True, autoload_with=engine)

for i in range(1, 5000000):
    start_date = date.today().replace(day=1, month=1).toordinal()
    end_date = date.today().toordinal()
    random_day = date.fromordinal(random.randint(start_date, end_date))
    order_date = random_day.strftime('%m/%d/%Y')
    irand = randrange(0,2)
    if(irand == 0):
        status = "CREATED"
    else:
        status = "COMPLETE"
    customer_id = randrange(1,10)
    ins = orders.insert().values(order_date=order_date, status=status, customer_id=customer_id)
    connection.execute(ins)

connection.close()
