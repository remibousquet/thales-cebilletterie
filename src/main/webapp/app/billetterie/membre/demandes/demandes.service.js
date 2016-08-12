(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('Demande', Demande);

    Demande.$inject = ['$resource', 'DateUtils', '$http'];

    function Demande ($resource, DateUtils, $http) {

    }
})();
