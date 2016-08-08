(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('enfant', {
            parent: 'entity',
            url: '/enfant',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.enfant.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/enfant/enfants.html',
                    controller: 'EnfantController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('enfant');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('enfant-detail', {
            parent: 'entity',
            url: '/enfant/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.enfant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/enfant/enfant-detail.html',
                    controller: 'EnfantDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('enfant');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Enfant', function($stateParams, Enfant) {
                    return Enfant.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'enfant',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('enfant-detail.edit', {
            parent: 'enfant-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enfant/enfant-dialog.html',
                    controller: 'EnfantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Enfant', function(Enfant) {
                            return Enfant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enfant.new', {
            parent: 'enfant',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enfant/enfant-dialog.html',
                    controller: 'EnfantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                prenom: null,
                                dateNaissance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('enfant', null, { reload: true });
                }, function() {
                    $state.go('enfant');
                });
            }]
        })
        .state('enfant.edit', {
            parent: 'enfant',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enfant/enfant-dialog.html',
                    controller: 'EnfantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Enfant', function(Enfant) {
                            return Enfant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('enfant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enfant.delete', {
            parent: 'enfant',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enfant/enfant-delete-dialog.html',
                    controller: 'EnfantDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Enfant', function(Enfant) {
                            return Enfant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('enfant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
