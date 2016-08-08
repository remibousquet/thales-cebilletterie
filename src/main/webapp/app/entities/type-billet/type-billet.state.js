(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-billet', {
            parent: 'entity',
            url: '/type-billet',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.typeBillet.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-billet/type-billets.html',
                    controller: 'TypeBilletController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeBillet');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-billet-detail', {
            parent: 'entity',
            url: '/type-billet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.typeBillet.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-billet/type-billet-detail.html',
                    controller: 'TypeBilletDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeBillet');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeBillet', function($stateParams, TypeBillet) {
                    return TypeBillet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-billet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-billet-detail.edit', {
            parent: 'type-billet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-billet/type-billet-dialog.html',
                    controller: 'TypeBilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeBillet', function(TypeBillet) {
                            return TypeBillet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-billet.new', {
            parent: 'type-billet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-billet/type-billet-dialog.html',
                    controller: 'TypeBilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-billet', null, { reload: true });
                }, function() {
                    $state.go('type-billet');
                });
            }]
        })
        .state('type-billet.edit', {
            parent: 'type-billet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-billet/type-billet-dialog.html',
                    controller: 'TypeBilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeBillet', function(TypeBillet) {
                            return TypeBillet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-billet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-billet.delete', {
            parent: 'type-billet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-billet/type-billet-delete-dialog.html',
                    controller: 'TypeBilletDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeBillet', function(TypeBillet) {
                            return TypeBillet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-billet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
