(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('EnfantDialogController', EnfantDialogController);

    EnfantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Enfant', 'User'];

    function EnfantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Enfant, User) {
        var vm = this;

        vm.enfant = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.enfant.id !== null) {
                Enfant.update(vm.enfant, onSaveSuccess, onSaveError);
            } else {
                Enfant.save(vm.enfant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:enfantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateNaissance = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
