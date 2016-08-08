(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('SubventionDeleteController',SubventionDeleteController);

    SubventionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Subvention'];

    function SubventionDeleteController($uibModalInstance, entity, Subvention) {
        var vm = this;

        vm.subvention = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Subvention.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
