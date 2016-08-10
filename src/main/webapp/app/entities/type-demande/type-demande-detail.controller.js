(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TypeDemandeDetailController', TypeDemandeDetailController);

    TypeDemandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeDemande'];

    function TypeDemandeDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeDemande) {
        var vm = this;

        vm.typeDemande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:typeDemandeUpdate', function(event, result) {
            vm.typeDemande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
