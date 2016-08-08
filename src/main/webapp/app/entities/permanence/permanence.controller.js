(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('PermanenceController', PermanenceController);

    PermanenceController.$inject = ['$scope', '$state', 'Permanence', 'PermanenceSearch'];

    function PermanenceController ($scope, $state, Permanence, PermanenceSearch) {
        var vm = this;
        
        vm.permanences = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Permanence.query(function(result) {
                vm.permanences = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PermanenceSearch.query({query: vm.searchQuery}, function(result) {
                vm.permanences = result;
            });
        }    }
})();
