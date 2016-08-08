(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('BilletController', BilletController);

    BilletController.$inject = ['$scope', '$state', 'Billet', 'BilletSearch'];

    function BilletController ($scope, $state, Billet, BilletSearch) {
        var vm = this;
        
        vm.billets = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Billet.query(function(result) {
                vm.billets = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BilletSearch.query({query: vm.searchQuery}, function(result) {
                vm.billets = result;
            });
        }    }
})();
