(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TypeDemandeController', TypeDemandeController);

    TypeDemandeController.$inject = ['$scope', '$state', 'TypeDemande', 'TypeDemandeSearch'];

    function TypeDemandeController ($scope, $state, TypeDemande, TypeDemandeSearch) {
        var vm = this;
        
        vm.typeDemandes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeDemande.query(function(result) {
                vm.typeDemandes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeDemandeSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeDemandes = result;
            });
        }    }
})();
