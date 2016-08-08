(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('PermanenceDeleteController',PermanenceDeleteController);

    PermanenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Permanence'];

    function PermanenceDeleteController($uibModalInstance, entity, Permanence) {
        var vm = this;

        vm.permanence = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Permanence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
