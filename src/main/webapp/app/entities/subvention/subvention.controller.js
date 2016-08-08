(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('SubventionController', SubventionController);

    SubventionController.$inject = ['$scope', '$state', 'Subvention', 'SubventionSearch'];

    function SubventionController ($scope, $state, Subvention, SubventionSearch) {
        var vm = this;
        
        vm.subventions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Subvention.query(function(result) {
                vm.subventions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SubventionSearch.query({query: vm.searchQuery}, function(result) {
                vm.subventions = result;
            });
        }    }
})();
