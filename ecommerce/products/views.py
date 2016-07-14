from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse, JsonResponse
from models import Medium
from django.db.models import F, Sum, Count
from django.core.serializers import serialize
import json

from django.http import HttpResponse
from django.core import serializers
import json

def as_json(query):
    now_date = query.get('order__order_date')
    now_date = now_date.split('-')
    now_date = now_date[0] + "/" + now_date[1] + "/" + now_date[2]
    return {
        "date": now_date,
        "orders": query.get('orders'),
        "qty": query.get('qty'),
        "buy_price" : query.get('buy_price'),
        "sale_price": query.get('sale_price'),
        "profit": query.get('profit')
    }

def index(request):
    return HttpResponse("Hello, world. You're at the Products index.")

def getResults(request):
    start_date = request.GET.get('startDate')
    end_date = request.GET.get('endDate')
    print start_date
    if start_date != None:
        start_date = start_date.split('/')
        start_date = start_date[2] + "-" + start_date[0] + "-" + start_date[1]
    if end_date != None:
        end_date = end_date.split('/')
        end_date = end_date[2] + "-" + end_date[0] + "-" + end_date[1]
    if end_date == None:
        end_date = '2020-10-10'
    if start_date == None:
        start_date = '2001-01-01'
    query = Medium.objects.filter(order__order_date__gte=start_date, order__order_date__lte=end_date).values(
        'order__order_date').annotate(orders=Count('order', distinct=True), qty=Sum('quantity'),
                                      sale_price=Sum(F('price') * F('quantity')),
                                      buy_price=Sum(F('quantity') * F('product__buy_price')),
                                      profit=Sum(F('price') * F('quantity')) - Sum(F('quantity') * F('product__buy_price'))).order_by('order__order_date').reverse()
    d = []
    for q in query:
        d.append(as_json(q))
    output = json.dumps({"data":d})
    output = json.dumps(json.loads(output), indent=4)

    return HttpResponse(output, content_type="application/json")