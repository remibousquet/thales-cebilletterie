(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TypeBilletController', TypeBilletController);

    TypeBilletController.$inject = ['$scope', '$state', 'TypeBillet', 'TypeBilletSearch'];

    function TypeBilletController ($scope, $state, TypeBillet, TypeBilletSearch) {
        var vm = this;
        
        vm.typeBillets = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeBillet.query(function(result) {
                vm.typeBillets = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeBilletSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeBillets = result;
            });
        }    }
})();
