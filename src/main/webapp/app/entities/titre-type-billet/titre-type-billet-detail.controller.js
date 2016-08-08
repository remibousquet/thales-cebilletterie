(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TitreTypeBilletDetailController', TitreTypeBilletDetailController);

    TitreTypeBilletDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TitreTypeBillet'];

    function TitreTypeBilletDetailController($scope, $rootScope, $stateParams, previousState, entity, TitreTypeBillet) {
        var vm = this;

        vm.titreTypeBillet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:titreTypeBilletUpdate', function(event, result) {
            vm.titreTypeBillet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
