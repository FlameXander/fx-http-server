angular.module('app', []).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8189';

    $scope.fillTable = function () {
        $http.get(contextPath + '/items')
            .then(function (response) {
                $scope.ProductsList = response.data;
            });
    };

    $scope.submitCreateNewProduct = function () {
        $http.post(contextPath + '/items', $scope.newProduct)
            .then(function (response) {
                $scope.fillTable();
            });
    };

    $scope.deleteProductById = function(productId) {
        $http({
            url: contextPath + '/items/' + productId,
            method: "DELETE"
        }).then(function (response) {
            $scope.fillTable();
        });
    }

    // $scope.tryToGetFile = function () {
    //     $http({
    //         url: contextPath + '/123.png',
    //         cache: true,
    //         method: "GET"
    //     }).then(function (response) {
    //         console.log('ok')
    //     });
    // };

    $scope.fillTable();

    // $scope.tryToGetFile();
    // $scope.tryToGetFile();
    // $scope.tryToGetFile();
});