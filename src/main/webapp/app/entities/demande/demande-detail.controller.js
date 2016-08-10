(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('DemandeDetailController', DemandeDetailController);

    DemandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Demande', 'StatutDemande', 'Paiement', 'Billet', 'TypeDemande'];

    function DemandeDetailController($scope, $rootScope, $stateParams, previousState, entity, Demande, StatutDemande, Paiement, Billet, TypeDemande) {
        var vm = this;

        vm.demande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:demandeUpdate', function(event, result) {
            vm.demande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
