'use strict';

describe('Controller Tests', function() {

    describe('Billet Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBillet, MockTypeBillet, MockTitreTypeBillet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBillet = jasmine.createSpy('MockBillet');
            MockTypeBillet = jasmine.createSpy('MockTypeBillet');
            MockTitreTypeBillet = jasmine.createSpy('MockTitreTypeBillet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Billet': MockBillet,
                'TypeBillet': MockTypeBillet,
                'TitreTypeBillet': MockTitreTypeBillet
            };
            createController = function() {
                $injector.get('$controller')("BilletDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cebilletterieApp:billetUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
