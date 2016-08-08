(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('PermanenceDialogController', PermanenceDialogController);

    PermanenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Permanence', 'User'];

    function PermanenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Permanence, User) {
        var vm = this;

        vm.permanence = entity;
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
            if (vm.permanence.id !== null) {
                Permanence.update(vm.permanence, onSaveSuccess, onSaveError);
            } else {
                Permanence.save(vm.permanence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:permanenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
