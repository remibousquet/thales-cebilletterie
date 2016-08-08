(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('StatutDemandeDetailController', StatutDemandeDetailController);

    StatutDemandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StatutDemande'];

    function StatutDemandeDetailController($scope, $rootScope, $stateParams, previousState, entity, StatutDemande) {
        var vm = this;

        vm.statutDemande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:statutDemandeUpdate', function(event, result) {
            vm.statutDemande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
