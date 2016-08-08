(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('DemandeController', DemandeController);

    DemandeController.$inject = ['$scope', '$state', 'Demande', 'DemandeSearch'];

    function DemandeController ($scope, $state, Demande, DemandeSearch) {
        var vm = this;
        
        vm.demandes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Demande.query(function(result) {
                vm.demandes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DemandeSearch.query({query: vm.searchQuery}, function(result) {
                vm.demandes = result;
            });
        }    }
})();
