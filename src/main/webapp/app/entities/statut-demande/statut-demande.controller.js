(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('StatutDemandeController', StatutDemandeController);

    StatutDemandeController.$inject = ['$scope', '$state', 'StatutDemande', 'StatutDemandeSearch'];

    function StatutDemandeController ($scope, $state, StatutDemande, StatutDemandeSearch) {
        var vm = this;
        
        vm.statutDemandes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            StatutDemande.query(function(result) {
                vm.statutDemandes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            StatutDemandeSearch.query({query: vm.searchQuery}, function(result) {
                vm.statutDemandes = result;
            });
        }    }
})();
