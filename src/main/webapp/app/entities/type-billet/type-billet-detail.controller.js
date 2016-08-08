(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TypeBilletDetailController', TypeBilletDetailController);

    TypeBilletDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeBillet'];

    function TypeBilletDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeBillet) {
        var vm = this;

        vm.typeBillet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:typeBilletUpdate', function(event, result) {
            vm.typeBillet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
