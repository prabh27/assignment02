/**
 * Created by prabh on 18/07/16.
 */

var app = angular.module('store', ['ngRoute', 'ngCookies']);

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.when('/', {
            templateUrl: 'products.html',
            controller: 'StoreController'
        }).when('/product:param', {
            templateUrl: 'single_product.html',
            controller: 'DetailsController'
        }).when('/view_cart', {
            templateUrl: 'view_cart.html',
            controller: 'ViewCartController'
        })
            .when('/checkout', {
                templateUrl: 'checkout.html',
                controller: 'SubmitOrderController'
            })
            .when('/order_summary', {
                templateUrl: 'order_summary.html',
                controller: 'ViewCartController'
            })
            .when('/feedback', {
                templateUrl: 'feedback.html',
                controller: 'FeedbackController'
            })
    }]);


app.controller("StoreController", function ($scope, $http, $routeParams, $cookies) {
    $scope.image_url = ["http://buyersguide.caranddriver.com/media/assets/submodel/280_7568.jpg",
        "http://buyersguide.caranddriver.com/media/assets/submodel/280_6731.jpg",
        "http://wallpapers55.com/wp-content/uploads/2013/08/Lexus-Police-Car-1024x609-280x170.jpg"];
    if ($cookies.get('items') == null || $cookies.get('items') == 0) {
        $cookies.put('items', 0);
        $scope.items = 0;
    }
    $scope.items = function () {
        return $cookies.get('items');
    }
    $http.get('http://127.0.0.1:8000/api/products/')
        .success(function (data, status, headers, config) {
            $scope.products = data;

        });
});

app.controller("DetailsController", function ($scope, $http, $routeParams, $cookies) {
    $scope.image_url = ["http://buyersguide.caranddriver.com/media/assets/submodel/280_7568.jpg",
        "http://buyersguide.caranddriver.com/media/assets/submodel/280_6731.jpg",
        "http://wallpapers55.com/wp-content/uploads/2013/08/Lexus-Police-Car-1024x609-280x170.jpg"];
    $scope.items = $cookies.get('items');
    param = $routeParams.param;
    url = 'http://127.0.0.1:8000/api/products/' + param;
    console.log(url);
    $http({
        url: url,
        method: "GET"
    })
        .success(function (data, status, headers, config) {
            $scope.product = data;
        });
});


app.controller("CategoryController", function ($scope, $http, $routeParams, $cookies) {
    $scope.items = $cookies.get('items');
    $http.get('http://127.0.0.1:8000/api/category/')
        .success(function (data, status, headers, config) {
            $scope.category = data.id;
            $scope.categories = data;
        });

    $scope.setCategory = function (category_id) {
        $scope.catFilter = category_id;
        console.log($scope.catFilter);
    }
});


app.controller("AddToCartController", function ($window, $cookies, $scope, $http, $routeParams) {
    $scope.image_url = ["http://buyersguide.caranddriver.com/media/assets/submodel/280_7568.jpg",
        "http://buyersguide.caranddriver.com/media/assets/submodel/280_6731.jpg",
        "http://wallpapers55.com/wp-content/uploads/2013/08/Lexus-Police-Car-1024x609-280x170.jpg"];
    $scope.obj = {};
    if ($cookies.get('items') == null) {
        $cookies.put('items', 0);
    }
    $scope.items = $cookies.get('items');
    $scope.addProduct = function (product_id, quantity, price) {
        if (quantity == undefined) {
            quantity = 1;
        }
        if ($cookies.get('order_id') == null) {
            $http({
                url: 'http://127.0.0.1:8000/api/orders/',
                method: "POST",
                data: {'status': "CREATED"}

            })
                .success(function (data, status, headers, config) {
                    $scope.order = data;
                    $cookies.put('order_id', $scope.order.id);
                });
        }
        url = 'http://127.0.0.1:8000/api/orders/' + $cookies.get('order_id') + '/orderlineitem/';
        console.log(url);

        $http({
            url: url,
            method: "POST",
            data: {
                "product_id": product_id,
                "price": price,
                "quantity": quantity
            },
        })
            .success(function (data, status, headers, config) {
                var count = parseInt($cookies.get('items'));
                count += parseInt(quantity);
                $cookies.put('items', count);
                console.log("items  " + $cookies.get('items'));
                $scope.items = count;

                console.log($scope.items);
            });
    }


});

app.controller("ViewCartController", function ($cookies, $scope, $http, $routeParams, $window) {
    $scope.image_url = ["http://buyersguide.caranddriver.com/media/assets/submodel/280_7568.jpg",
        "http://buyersguide.caranddriver.com/media/assets/submodel/280_6731.jpg",
        "http://wallpapers55.com/wp-content/uploads/2013/08/Lexus-Police-Car-1024x609-280x170.jpg"];
    $scope.items = $cookies.get('items');
    url = 'http://127.0.0.1:8000/api/orders/' + $cookies.get('order_id') + '/orderlineitem/';
    $scope.range = function (min, max, step) {
        step = step || 1;
        var input = [];
        for (var i = min; i <= max; i += step) {
            input.push(i);
        }
        return input;
    };
    $scope.products = [];
    $scope.details = [];
    $scope.cost = 0;
    $scope.order_id = $cookies.get('order_id');
    $http({
        method: 'GET',
        url: url
    })
        .then(function successCallback(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.details.push(response.data[i]);
                $scope.cost += ($scope.details[i].fields.quantity) * ($scope.details[i].fields.price);
                console.log($scope.details[i].fields.quantity);
            }
            for (var i = 0; i < response.data.length; i++) {
                $http({
                    method: 'GET',
                    url: 'http://localhost:8000/api/products/' + $scope.details[i].fields.product
                }).then(function successCallback(response) {
                    $scope.cost = Math.round($scope.cost * 100) / 100;
                    $scope.products.push(response.data);
                });
            }
        }, function errorCallback(response) {
            console.log("Error")
        });


    $scope.emptyCart = function () {
        $cookies.remove('order_id');
        $cookies.put('items', 0);
    }


});

app.controller("SubmitOrderController", function ($cookies, $scope, $http, $window) {
    $scope.items = $cookies.get('items');
    $scope.submitOrder = function () {
        console.log($scope.address);
        url = 'http://127.0.0.1:8000/api/orders/' + $cookies.get('order_id') + '/';
        $http({
            url: url,
            method: "PATCH",
            data: {
                "username": $scope.username,
                "address": $scope.address,
                "status": "Complete"
            },
        })
            .success(function (data, status, headers, config) {
                console.log(data);
                $window.location.href = '#/order_summary';
            });

    }
});


app.controller("FeedbackController", function ($cookies, $scope, $http, $window) {
    $scope.contactUs = function (email, description) {
        console.log(description);
        console.log(email);
        url = 'http://127.0.0.1:8000/api/feedback/';
        $http({
            url: url,
            method: "POST",
            data: {
                "description": description,
                "email_address": email,
            },
        })
            .success(function (data, status, headers, config) {
                console.log(data);
                $window.alert("Success");
                $window.location.href = '#/';
            });
    }

});



