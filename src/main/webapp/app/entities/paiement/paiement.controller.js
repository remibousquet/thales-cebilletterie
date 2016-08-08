(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('PaiementController', PaiementController);

    PaiementController.$inject = ['$scope', '$state', 'Paiement', 'PaiementSearch'];

    function PaiementController ($scope, $state, Paiement, PaiementSearch) {
        var vm = this;
        
        vm.paiements = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Paiement.query(function(result) {
                vm.paiements = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PaiementSearch.query({query: vm.searchQuery}, function(result) {
                vm.paiements = result;
            });
        }    }
})();
