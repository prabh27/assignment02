from rest_framework import serializers

from models import Products, Category, Orders, Bridge, Medium, Customers

from time import gmtime, strftime


class ProductsSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(source='product_id', read_only=True)
    code = serializers.CharField(source='product_code', required=True)
    description = serializers.CharField(required=True)
    price = serializers.FloatField(source='buy_price', required=True)
    category_id = serializers.CharField(read_only=True, source='get_category_id')
    category = serializers.CharField(required=True, write_only=True)

    def create(self, validated_data):
        product = Products.objects.get_or_create(product_code=validated_data['product_code'])[0];
        product.description = validated_data['description']
        product.buy_price = validated_data['buy_price']
        product.quantity = 500
        product.save()
        category = Category.objects.get_or_create(name=validated_data['category'])
        bridge = Bridge.objects.create(product=product,
                                       category=Category.objects.get_or_create(name=validated_data['category'])[0])
        return product

    def update(self, instance, validated_data):
        if not self.partial:
            #Put
            instance.product_code = validated_data['product_code']
            instance.description = validated_data['description']
            instance.buy_price = validated_data['buy_price']
            instance.category = Category.objects.get_or_create(name=validated_data['category'])
            Bridge.objects.get_or_create(product=instance,
                                         category=Category.objects.get_or_create(name=validated_data['category'])[0])
            instance.save()
            return instance
        else:
            #Patch
            if validated_data.get('product_code'):
                instance.product_code = validated_data['product_code']
            if validated_data.get('description'):
                instance.description = validated_data['description']
            if validated_data.get('buy_price'):
                instance.buy_price = validated_data['buy_price']
            if validated_data.get('category'):
                instance.category = Category.objects.get_or_create(name=validated_data['category'])
                Bridge.objects.get_or_create(product=instance,
                                             category=Category.objects.get_or_create(name=validated_data['category'])[
                                                 0])
            instance.save()
            return instance

    class Meta:
        model = Products
        fields = ('id', 'code', 'description', 'price', 'category_id', 'category')


class OrderLineSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(source='medium_id', read_only=True)
    product_id = serializers.IntegerField(source='product.product_id', required=True)
    order_id = serializers.IntegerField(source='order.order_id', read_only=True)
    price = serializers.IntegerField(required=True)

    class Meta:
        model = Medium
        fields = ('id', 'product_id', 'order_id', 'price')


class OrdersSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(source='order_id', read_only=True)
    username = serializers.CharField(source='customer.customer_name', required=False)
    address = serializers.CharField(source='customer.address_line1', required=False)
    status = serializers.CharField()

    class Meta:
        model = Orders
        fields = ('id', 'username', 'address', 'status')

    def create(self, validated_data):
        if not validated_data.get('customer'):
            return Orders.objects.create(order_date=strftime("%Y-%m-%d", gmtime()),
                                         status=validated_data['status'])
        else:
            if validated_data.get('customer').get('customer_name'):
                customer = Customers.objects.get_or_create(customer_name=validated_data.get('customer')['customer_name'])[0]
            else:
                customer = Customers.objects.get(customer_name="null")
            if validated_data.get('customer').get('address_line1'):
                customer.address_line1 = validated_data.get('customer').get('address_line1')
            customer.save()
            return Orders.objects.create(customer=customer,
                                         order_date=strftime("%Y-%m-%d", gmtime()),
                                         status=validated_data['status'])



    def update(self, instance, validated_data):
            #Put & Patch
            if not validated_data.get('customer'):
                if 'status' in validated_data:
                    instance.status = validated_data['status']
                if not self.partial:
                    instance.customer = None
                instance.save()
            else:
                if validated_data.get('customer').get('customer_name'):
                    instance.customer = Customers.objects.get_or_create(customer_name=validated_data.get('customer')['customer_name'])[0]
                    print instance.customer.address_line1
                else:
                    if(instance.customer == None):
                        instance.customer = Customers.objects.create(customer_name="null")
                if validated_data.get('customer').get('address_line1'):
                    instance.customer.address_line1 = validated_data.get('customer').get('address_line1')
                else:
                    if not self.partial:
                         instance.customer.address_line1 = None
                instance.status = validated_data['status']
                instance.customer.save()
                instance.save()
            return instance
