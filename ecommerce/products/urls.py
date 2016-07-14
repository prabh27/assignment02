from django.conf.urls import url

from . import views
from views import getResults

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^reports/daily-sale', getResults)
]