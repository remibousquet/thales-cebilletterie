(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('SubventionDialogController', SubventionDialogController);

    SubventionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Subvention', 'User'];

    function SubventionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Subvention, User) {
        var vm = this;

        vm.subvention = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.subvention.id !== null) {
                Subvention.update(vm.subvention, onSaveSuccess, onSaveError);
            } else {
                Subvention.save(vm.subvention, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:subventionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
