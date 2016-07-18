
# Register your models here.
from django.contrib import admin

from . import models
# Register your models here.
admin.site.register(models.Products)
admin.site.register(models.Category)
admin.site.register(models.Medium)
admin.site.register(models.Bridge)