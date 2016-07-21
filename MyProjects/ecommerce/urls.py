from django.conf.urls import url

from . import views
from views import getResults
from views import productSummary

from django.conf.urls import url, include, patterns


from rest_framework import routers
from views import ProductsViewSet
from views import OrdersViewSet
from views import OrderLineViewSet
from views import health



router = routers.DefaultRouter()
router.register(r'products', ProductsViewSet)
router.register(r'orders/(?P<order_id>[0-9]+)/orderlineitem', OrderLineViewSet)
router.register(r'orders', OrdersViewSet)



urlpatterns = [
    url(r'^reports/daily-sale', getResults),
    url(r'^health', health),
    url(r'^', include(router.urls)),
    url(r'^docs/', include('rest_framework_swagger.urls')),
    url(r'^products/summary', productSummary)

]