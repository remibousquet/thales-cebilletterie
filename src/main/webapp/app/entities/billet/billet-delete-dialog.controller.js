(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('BilletDeleteController',BilletDeleteController);

    BilletDeleteController.$inject = ['$uibModalInstance', 'entity', 'Billet'];

    function BilletDeleteController($uibModalInstance, entity, Billet) {
        var vm = this;

        vm.billet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Billet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
