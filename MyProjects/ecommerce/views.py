from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse, JsonResponse
from models import Medium
from django.db.models import F, Sum, Count, Q
from django.core.serializers import serialize
from rest_framework import status

from rest_framework import generics

import json

from rest_framework.decorators import detail_route, list_route


from django.http import HttpResponse
from django.http import Http404
from django.core import serializers

from rest_framework import viewsets
from rest_framework import mixins
from models import Products, Orders, Bridge

from .serializers import ProductsSerializer, OrdersSerializer, OrderLineSerializer
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

def my_health(self):
    return HttpResponse(status=status.HTTP_200_OK);

def getResults(request):
    start_date = request.GET.get('startDate')
    end_date = request.GET.get('endDate')
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


def productSummary(request):
    data = {}
    code = request.GET.get('code')
    category_name = request.GET.get('category_name')
    group_by = request.GET.get('group_by')
    querySet = Bridge.objects.all()
    if code == None and category_name == None and group_by == None:
        querySet = Products.objects.all().annotate(count=Count('product_id'))
        data['count'] = len(querySet)
        d = {}
        d['data'] = data
        print data
        return JsonResponse(d)
    if code != None:
        querySet = querySet.filter(product__product_code=code)
    if category_name != None:
        querySet = querySet.filter(category__name=category_name)
    if group_by != None:
        querySet = querySet.values('category__category_id').annotate(count=Count('product__product_id', distinct=True))
    else:
        querySet = querySet.annotate(count=Count('order__order_id', distinct=True))
    d = {}
    data = list(querySet)
    d['data'] = data
    return JsonResponse(d)

def OrdersSummary(request):
    data = {}
    group_by = request.GET.get('group_by')
    product_code = request.GET.get('orderlineitem__product_code')
    category_name = request.GET.get('orderlineitem_category_name')
    querySet = Bridge.objects.all()
    if product_code != None:
        querySet = querySet.filter(product__product_code=product_code)
    if category_name != None:
        querySet = querySet.filter(category__name=category_name)
    if group_by == None:
        data = len(querySet)
    elif group_by == 'category':
        querySet = querySet.values('category__category_id').annotate(count=Count('category__category_id', distinct=True))
    else:
        querySet = querySet.values('product__product_id').annotate(count=Count('product__product_id', distinct=True))
    d = {}
    data = list(querySet)
    d['data'] = data
    return JsonResponse(d)




class ProductsViewSet(viewsets.ModelViewSet):
    queryset = Products.objects.filter(Q(is_available=1))
    serializer_class = ProductsSerializer

    def destroy(self, request, *args, **kwargs):
        try:
            instance = self.get_object()
            instance.is_available = 0
            instance.save()
        except Http404:
            return HttpResponse(status=status.HTTP_404_NOT_FOUND)
        return HttpResponse(status=status.HTTP_204_NO_CONTENT)


class OrdersViewSet(viewsets.ModelViewSet):
    queryset = Orders.objects.filter(Q(is_available=1))
    serializer_class = OrdersSerializer

    def destroy(self, request, *args, **kwargs):
        try:
            instance = self.get_object()
            instance.is_available = 0
            instance.save()
        except Http404:
            return HttpResponse(status=status.HTTP_404_NOT_FOUND)
        return HttpResponse(status=status.HTTP_204_NO_CONTENT)



class OrderLineViewSet(mixins.RetrieveModelMixin, mixins.CreateModelMixin, mixins.ListModelMixin, viewsets.GenericViewSet):
    queryset = Medium.objects.all()
    serializer_class = OrderLineSerializer

    def create(self, request, *args, **kwargs):
        orderLineSerializer = OrderLineSerializer(data=request.data)
        data = request.data
        print data
        if orderLineSerializer.is_valid():
            print "aaya"
            product = Products.objects.get(product_id=data['product_id'])
            order = Orders.objects.get(order_id=kwargs['order_id'])
            medium = Medium.objects.create(product=product, order=order, price=data['price'])
            serializer = OrderLineSerializer(medium)
            return HttpResponse(serializer, status=status.HTTP_201_CREATED)
        else:
            return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    def retrieve(self, request, *args, **kwargs):
        queryset = Medium.objects.filter(order__order_id=kwargs['order_id'], medium_id=kwargs['pk'])
        try:
            return HttpResponse(serializers.serialize('json', queryset=queryset, many=True))
        except:
            return HttpResponse(serializers.serialize('json', queryset=queryset))

    def list(self, request, *args, **kwargs):
        queryset = Medium.objects.filter(order__order_id=kwargs['order_id'])
        try:
            return HttpResponse(serializers.serialize('json', queryset=queryset, many=True))
        except:
            return HttpResponse(serializers.serialize('json', queryset=queryset))


class MiddleWare(object):
    def process_response(self, request, response):
        if(response is not None):
            if(response._container is not None and response._container is not ['']):
                response._container = ['{"data":'+response._container[0]+'}']

        return response




