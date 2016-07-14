# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey has `on_delete` set to the desired behavior.
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from __future__ import unicode_literals

from django.db import models



class Auditlog(models.Model):
    audit_id = models.IntegerField(primary_key=True)
    timestamp = models.TextField(blank=True, null=True)
    url = models.CharField(max_length=255, blank=True, null=True)
    response_code = models.IntegerField(blank=True, null=True)
    parameters = models.CharField(max_length=255, blank=True, null=True)
    request_type = models.CharField(max_length=255, blank=True, null=True)
    request_duration_ms = models.CharField(max_length=255, blank=True, null=True)
    request_ip_address = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'AuditLog'


class Category(models.Model):
    category_id = models.IntegerField(primary_key=True)
    description = models.TextField(blank=True, null=True)
    product = models.ForeignKey('Products', models.DO_NOTHING)

    def __unicode__(self):
        return "Category: %d" % (self.category_id)

    class Meta:
        managed = False
        db_table = 'Category'


class Customers(models.Model):
    customer_id = models.AutoField(primary_key=True)
    customer_name = models.CharField(unique=True, max_length=255, blank=True, null=True)
    phone = models.CharField(max_length=255, blank=True, null=True)
    address_line1 = models.CharField(max_length=255, blank=True, null=True)
    address_line2 = models.CharField(max_length=255, blank=True, null=True)
    country = models.CharField(max_length=255, blank=True, null=True)
    postal_code = models.CharField(max_length=255, blank=True, null=True)
    email_address = models.CharField(max_length=255, blank=True, null=True)
    city = models.CharField(max_length=255, blank=True, null=True)
    contact_last_name = models.CharField(max_length=255, blank=True, null=True)
    state = models.CharField(max_length=255, blank=True, null=True)
    contact_first_name = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'Customers'


class Feedback(models.Model):
    feedback_id = models.AutoField(primary_key=True)
    description = models.CharField(max_length=255, blank=True, null=True)
    email_address = models.CharField(max_length=255, blank=True, null=True)
    customer = models.ForeignKey(Customers, models.DO_NOTHING, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'Feedback'


class Medium(models.Model):
    order = models.ForeignKey('Orders', models.DO_NOTHING)
    product = models.ForeignKey('Products', models.DO_NOTHING)
    quantity = models.FloatField(blank=True, null=True)
    price = models.FloatField(blank=True, null=True)
    medium_id = models.AutoField(primary_key=True)

    class Meta:
        managed = False
        db_table = 'Medium'


class Orders(models.Model):
    order_id = models.AutoField(primary_key=True)
    order_date = models.CharField(max_length=255, blank=True, null=True)
    customer = models.ForeignKey(Customers, models.DO_NOTHING, blank=True, null=True)
    status = models.CharField(max_length=255, blank=True, null=True)
    is_available = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'Orders'


class Products(models.Model):
    product_code = models.CharField(max_length=255, blank=True, null=True)
    product_name = models.CharField(max_length=255, blank=True, null=True)
    buy_price = models.FloatField(blank=True, null=True)
    quantity = models.FloatField(blank=True, null=True)
    sell_price = models.FloatField(blank=True, null=True)
    description = models.TextField(blank=True, null=True)
    product_id = models.AutoField(primary_key=True)
    is_available = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'Products'


    def __unicode__(self):
        return "Product: %s" % (self.product_name)



