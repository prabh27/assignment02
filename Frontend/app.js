/**
 * Created by prabh on 18/07/16.
 */

var app = angular.module('store', ['ngRoute']);

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
        when('/', {
            templateUrl: 'products.html',
            controller: 'StoreController'
        }).
        when('/:param', {
            templateUrl: 'single_product.html',
            controller: 'DetailsController'
        });
    }]);


app.controller("StoreController", function ($scope, $http, $routeParams) {
        $http.get('http://127.0.0.1:8000/api/products/')
            .success(function (data, status, headers, config) {
                $scope.products = data;
                console.log($scope.products);
            });
});

app.controller("DetailsController", function ($scope, $http, $routeParams) {
    param = $routeParams.param;
    url = 'http://127.0.0.1:8000/api/products/' + param;
    console.log(url);
    $http({
        url: url,
        method: "GET"
    })
        .success(function (data, status, headers, config) {
            $scope.product = data;
            console.log($scope.product);
        });
});

app.controller("CategoryController", function ($scope, $http, $routeParams) {
    $http.get('http://127.0.0.1:8000/api/category/')
        .success(function (data, status, headers, config) {
            console.log(data);
            $scope.categories = data;
            console.log($scope.categories);
        });
});