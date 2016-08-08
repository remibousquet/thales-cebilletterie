(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('EnfantController', EnfantController);

    EnfantController.$inject = ['$scope', '$state', 'Enfant', 'EnfantSearch'];

    function EnfantController ($scope, $state, Enfant, EnfantSearch) {
        var vm = this;
        
        vm.enfants = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Enfant.query(function(result) {
                vm.enfants = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnfantSearch.query({query: vm.searchQuery}, function(result) {
                vm.enfants = result;
            });
        }    }
})();
